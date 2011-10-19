package com.maxifier.mxcache.impl.resource;

import com.maxifier.mxcache.caches.CleaningNode;
import com.maxifier.mxcache.util.TIdentityHashSet;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.Reference;

/**
 * Created by IntelliJ IDEA.
 * User: dalex
 * Date: 14.04.2010
 * Time: 10:45:03
 */
public interface DependencyNode {
    /**
     * Each node must maintain a reference to itself. This reference should have equals and hashCode implemented to
     * compare corresponding referent objects. This reference is used when adding a dependency to node.
     * @return reference to this node.
     */
    Reference<DependencyNode> getSelfReference();

    /**
     * Should pass all dependent nodes to visitor.
     * @param visitor visitor
     */
    void visitDependantNodes(DependencyNodeVisitor visitor);

    /**
     * ��������� � ������ ��� ����, ������� ������ ���� ������� ���� �����
     * (������ �� �����, ��� ����� ��������� �� ����).
     * @param elements ������ �����
     */
    void appendNodes(TIdentityHashSet<CleaningNode> elements);

    /**
     * ��������� �����������.
     * @param node ����� ��������� ����
     */
    void trackDependency(DependencyNode node);

    /**
     * ��������� � ���� ���, ������� ����� �������.
     * @param cache ���
     */
    void addNode(@NotNull CleaningNode cache);
}
